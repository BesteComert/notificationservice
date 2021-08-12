package com.buggerspace.notificationservice.repository;


import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.stereotype.Repository;

import com.buggerspace.notificationservice.entity.UserNotification;
import com.couchbase.client.java.query.QueryScanConsistency;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationRepository extends CouchbaseRepository<UserNotification, Long> {

	Object deleteByEmail(String email);

	@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} and userId = $1")
	List<UserNotification> findByUserId(Long id);


	@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
	boolean existsByEmail(String email);

}
