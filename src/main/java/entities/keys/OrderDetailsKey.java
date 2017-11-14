package entities.keys;

import java.io.Serializable;

public class OrderDetailsKey implements Serializable {
    protected int orderId;
    protected int productId;

    public OrderDetailsKey() {
    }

    public OrderDetailsKey(int orderId, int productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderDetailsKey that = (OrderDetailsKey) o;

        if (orderId != that.orderId) return false;
        return productId == that.productId;
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + productId;
        return result;
    }
}
