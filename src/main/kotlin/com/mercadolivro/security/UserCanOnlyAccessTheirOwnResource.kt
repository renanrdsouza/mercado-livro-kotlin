package com.mercadolivro.security

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("hasRole('ADMIN_ROLE') || #id == authetication.principal.id")
annotation class UserCanOnlyAccessTheirOwnResource
