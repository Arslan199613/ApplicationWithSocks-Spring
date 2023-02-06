package com.example.applicationwithsocks.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Socks {

    @NotBlank
    private Color color;
    @NotBlank
    private Size size;
    @Positive
    @NotEmpty
    private int quantity;
    @Positive
    @NotEmpty
    private int cottonPartAsAPercentage;

    @NotBlank
    private Type type;

    @Override
    public String toString() {
        return type + " " + color + " " + size + " " + quantity + " " + cottonPartAsAPercentage;
    }
}