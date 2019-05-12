package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addReview(Review review);

    @Query("select COUNT(rating) from review where roadside_assistant_username = :username or customer_username = :username")
    int getReviewCount(String username);

    @Query("select AVG(rating) from review where roadside_assistant_username = :username or customer_username = :username")
    float getAvgRating(String username);
}
