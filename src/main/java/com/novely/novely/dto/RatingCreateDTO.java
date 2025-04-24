package com.novely.novely.dto;

import java.io.Serializable;

import com.novely.novely.domain.Rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class RatingCreateDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Min(value = 1, message = "Rate must be at least 1")
    @Max(value = 5, message = "Rate must be at most 5")
    private int rate;

    public RatingCreateDTO() {

    }

    public RatingCreateDTO(Rating obj) {
        this.rate = obj.getRate();
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
