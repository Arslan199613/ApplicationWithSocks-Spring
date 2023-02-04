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
    @NotEmpty(message = "Данное поле не может быть пустым")
    private Color color;
    @NotEmpty(message = "Данное поле не может быть пустым")
    private Size size;
    @Positive
    private  int quantity;
    @Positive
    @NotEmpty
    @Min(value = 0,message = "Процентное содержание хлопка не может быть меньше 0")
    @Max(value = 100,message = "Процентное содержание хлопка не может быть больше 100%")
    private int cottonPartAsAPercentage;

    @NotEmpty
    private Type type;

    @Override
    public String toString() {
        return type+ " " +color + " " + size + " " + quantity + " " + cottonPartAsAPercentage;
    }
}