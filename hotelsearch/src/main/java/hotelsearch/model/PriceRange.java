package hotelsearch.model;

public class PriceRange {
    private Double maxPrice;
    private Double minPrice;

    public PriceRange() { }

    public PriceRange(Double maxPrice, Double minPrice) {
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }
}
