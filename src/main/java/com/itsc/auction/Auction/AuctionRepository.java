package com.itsc.auction.Auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByStatus(String status);
    List<Auction> findByEndTimeBeforeAndStatus(LocalDateTime endTime, String status);
    @Query("SELECT a FROM Auction a WHERE a.item.owner.username = :username")
    List<Auction> findByItemOwnerUsername(@Param("username") String username);

}
