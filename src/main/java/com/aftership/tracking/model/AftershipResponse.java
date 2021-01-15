package com.aftership.tracking.model;

import lombok.Data;

@Data
public class AftershipResponse<T> {
    private Meta meta;
    private T data;
}
