package iwish.iwish;

/**
 * Created by Visava on 8/10/2015.
 */
public class product {
    String code;
    String name;
    String description;
    String categorize;
    Double netweight;
    Double price;
    int amountOf;
    Integer amount;
    Double promoprice;
    boolean status;

    public product() {

    }

    public product(String code, String name, String description, String categorize, Double netweight, Double price, int amountOf) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.categorize = categorize;
        this.netweight = netweight;
        this.price = price;
        this.amountOf = amountOf;
        this.status = false;
    }

    public product(String code, String name, String description, String categorize, Double netweight, Double price, Integer amount, Double promoprice, int amountOf) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.categorize = categorize;
        this.netweight = netweight;
        this.price = price;
        this.amount = amount;
        this.promoprice = promoprice;
        this.amountOf = amountOf;
        this.status = false;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategorize() {
        return categorize;
    }

    public void setCategorize(String categorize) {
        this.categorize = categorize;
    }

    public Double getNetweight() {
        return netweight;
    }

    public void setNetweight(Double netweight) {
        this.netweight = netweight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getAmountOf() {
        return amountOf;
    }

    public void setAmountOf(int amountOf) {
        this.amountOf = amountOf;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPromoprice() {
        return promoprice;
    }

    public void setPromoprice(Double promoprice) {
        this.promoprice = promoprice;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        product that = (product) o;

        if (amountOf != that.amountOf) return false;
        if (status != that.status) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (categorize != null ? !categorize.equals(that.categorize) : that.categorize != null)
            return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (netweight != null ? !netweight.equals(that.netweight) : that.netweight != null)
            return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (promoprice != null ? !promoprice.equals(that.promoprice) : that.promoprice != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (categorize != null ? categorize.hashCode() : 0);
        result = 31 * result + (netweight != null ? netweight.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + amountOf;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (promoprice != null ? promoprice.hashCode() : 0);
        result = 31 * result + (status ? 1 : 0);
        return result;
    }
}
