package com.hobom.hobominternal.exception

class ArticleNotFoundException(slug: String) : ApplicationException("Article Not Found ! $slug")
