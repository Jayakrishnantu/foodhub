package com.foodhub.payload;

/**
 * Payload Carrying Create Item Request
 */
public class OrderCreateItem {

    private Long item;
    private Integer qty;
    private String instruction;

    public OrderCreateItem() {
    }

    public OrderCreateItem(Long item, Integer qty, String instruction) {
        this.item = item;
        this.qty = qty;
        this.instruction = instruction;
    }

    public Long getItem() {
        return item;
    }

    public void setItem(Long item) {
        this.item = item;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    @Override
    public String toString() {
        return "OrderCreateItem{" +
                "item=" + item +
                ", qty=" + qty +
                ", instruction='" + instruction + '\'' +
                '}';
    }
}
