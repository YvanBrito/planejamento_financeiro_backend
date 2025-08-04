package com.exemplo.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetRequest {
    BigDecimal value;
    String type;
    String date;
    Long category_id;
    Long subcategory_id;
}
