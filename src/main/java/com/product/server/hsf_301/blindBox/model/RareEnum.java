package com.product.server.hsf_301.blindBox.model;

public enum RareEnum {
     COMMON("Common"),
     UNCOMMON("Uncommon"),
     RARE("Rare"),
     SPECIAL("Special"),
     GOOD_LUCK("GoodLuck");

     private String value;

     RareEnum(String value) {
          this.value = value;
     }

     public String getValue() {
          return value;
     }

     // Note: It's generally unusual for enums to have setters,
     // but here is an example as requested.
     public void setValue(String value) {
          this.value = value;
     }
}