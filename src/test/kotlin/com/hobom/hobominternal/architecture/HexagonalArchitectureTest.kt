package com.hobom.hobominternal.architecture

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

@AnalyzeClasses(
    packages = ["com.hobom.hobominternal"],
    importOptions = [
        ImportOption.DoNotIncludeTests::class,
        ImportOption.DoNotIncludeJars::class,
    ],
)
class HexagonalArchitectureTest {

    companion object {
        private const val DOMAIN = "..domain.."
        private const val APPLICATION = "..application.."
        private const val PORT_IN = "..application.port.in.."
        private const val ADAPTER = "..adapter.."
        private const val ADAPTER_IN = "..adapter.in.."
        private const val ADAPTER_OUT = "..adapter.out.."
        private const val CONFIG = "..config.."
    }

    /** Domain은 Application, Adapter에 의존하지 않는다 */
    @ArchTest
    val `Domain must be independent` =
        noClasses()
            .that()
            .resideInAPackage(DOMAIN)
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(APPLICATION, ADAPTER)

    /** Application은 Domain과 표준 라이브러리만 참조 가능 */
    @ArchTest
    val `Application depends only on domain and standard libraries` =
        classes().that().resideInAPackage(APPLICATION)
            .should().onlyDependOnClassesThat().resideInAnyPackage(
                APPLICATION,
                DOMAIN,
                "java..",
                "javax..",
                "kotlin..",
                "org.jetbrains..",
            )

    /** Domain, Application, Config에서는 Adapter를 참조할 수 없다 */
    @ArchTest
    val `Adapters are dependency sinks` =
        noClasses()
            .that()
            .resideInAnyPackage(DOMAIN, APPLICATION, CONFIG)
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(ADAPTER)

    /** Config는 외부로 노출되어선 안 된다 */
    @ArchTest
    val `Config must not leak` =
        noClasses()
            .that()
            .resideOutsideOfPackage(CONFIG)
            .should()
            .dependOnClassesThat()
            .resideInAPackage(CONFIG)

    /** Inbound Adapter 네이밍 규칙 */
    @ArchTest
    val `Inbound adapters must follow naming convention` =
        classes()
            .that()
            .resideInAPackage(ADAPTER_IN)
            .should().haveSimpleNameEndingWith("Controller")
            .orShould().haveSimpleNameEndingWith("Handler")

    /** Outbound Adapter 네이밍 규칙 */
    @ArchTest
    val `Outbound adapters must follow naming convention` =
        classes()
            .that()
            .resideInAPackage(ADAPTER_OUT)
            .should()
            .haveSimpleNameEndingWith("Repository")
            .orShould()
            .haveSimpleNameEndingWith("Client")

    /** Inbound Port는 단일 메서드 인터페이스만 허용 */
    @ArchTest
    val `Inbound ports should be single-method interfaces` =
        classes()
            .that()
            .resideInAPackage(PORT_IN)
            .and()
            .areInterfaces()
            .should(haveExactlyOneDeclaredMethod())
}

fun haveExactlyOneDeclaredMethod(): ArchCondition<JavaClass> {
    return object : ArchCondition<JavaClass>("have exactly one declared method") {
        override fun check(javaClass: JavaClass, events: ConditionEvents) {
            val declaredMethods = javaClass.methods.filter { it.owner == javaClass }
            if (declaredMethods.size != 1) {
                events.add(
                    SimpleConditionEvent.violated(
                        javaClass,
                        "${javaClass.name} has ${declaredMethods.size} declared methods (expected 1)",
                    ),
                )
            }
        }
    }
}
