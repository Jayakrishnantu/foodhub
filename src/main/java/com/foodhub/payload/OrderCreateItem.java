package com.foodhub.payload;

public class OrderCreateItem {

        private Long item;
        private Integer qty;

        public OrderCreateItem() {
        }

        public OrderCreateItem(Long item, Integer qty) {
            this.item = item;
            this.qty = qty;
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

        @Override
        public String toString() {
            return "Item{" +
                    "item=" + item +
                    ", qty=" + qty +
                    '}';
        }
}
