package com.example.demo

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.persistence.Embeddable
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

@MappedSuperclass
abstract class BaseEntity<T: RdbEntity> : java.io.Serializable, RdbEntity {
    abstract var audit: RdbAuditModel?
    @PrePersist
    protected fun onCreate() {
        val now = LocalDateTime.now()
        val audit = RdbAuditModel(createdDate = now, updatedDate = now)
        this.audit = audit
    }
    @PreUpdate
    protected fun onUpdate() {
        val now = LocalDateTime.now()
        this.audit!!.updatedDate = now
    }
    @Embeddable
    class RdbAuditModel(
        /** 登録日時 */
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        var createdDate: LocalDateTime? = null,
        /** 更新日時 */
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        var updatedDate: LocalDateTime? = null
    ): java.io.Serializable
}