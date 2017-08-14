package ru.ulmc.bank.pojo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class CurrencyPairDto {
    private String base;
    private String quoted;
    private String symbol;
    private BigDecimal b1;
    private BigDecimal s1;
    private Date createDate;
    private Date changeDate;
    private String creatorName;
    private String modifierName;
    private Boolean active;
    private Boolean available;
}
