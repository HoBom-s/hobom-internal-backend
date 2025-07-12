package com.hobom.hobominternal.exception

class DlqMessageNotFoundException(id: Long) : ApplicationException("Dlq Message Not Found $id")
