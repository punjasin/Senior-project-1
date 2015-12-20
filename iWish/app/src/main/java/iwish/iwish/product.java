package iwish.iwish;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Visava on 8/10/2015.
 */
public class product implements Serializable, Comparator<product> {
    String code;
    String name;
    String description;
    String categorize;
    Double netweight;
    Double price;
    int amountOf;
    Integer amount;
    Double promoprice;
    Integer zone;
    Integer shelf;
    boolean status;
    String Image;
    Integer distant;

    public product() {

    }

    public product(String code, String name, String description, String categorize, Double netweight, Double price, Integer zone, Integer shelf, int amountOf, String Image, Integer distant) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.categorize = categorize;
        this.netweight = netweight;
        this.price = price;
        this.zone = zone;
        this.shelf = shelf;
        this.amountOf = amountOf;
        this.status = false;
        this.Image = Image;
        this.distant = distant;
    }

    public product(String code, String name, String description, String categorize, Double netweight, Double price, Integer amount, Double promoprice, Integer zone, Integer shelf, int amountOf, String Image, Integer distant) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.categorize = categorize;
        this.netweight = netweight;
        this.price = price;
        this.amount = amount;
        this.promoprice = promoprice;
        this.zone = zone;
        this.shelf = shelf;
        this.amountOf = amountOf;
        this.status = false;
        this.Image = Image;
        this.distant = distant;

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

    public Integer getZone() {
        return zone;
    }

    public void setZone(Integer zone) {
        this.zone = zone;
    }

    public Integer getShelf() {
        return shelf;
    }

    public void setShelf(Integer shelf) {
        this.shelf = shelf;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Integer getDistant() {
        return distant;
    }

    public void setDistant(Integer distant) {
        this.distant = distant;
    }

    @Override
    public int compare(product one, product another){
        int returnVal = 0;

        if(one.getDistant() < another.getDistant()){
            returnVal =  -1;
        }else if(one.getDistant() > another.getDistant()){
            returnVal =  1;
        }else if(one.getDistant() == another.getDistant()){
            returnVal =  0;
        }
        return returnVal;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        product product = (product) o;

        if (amountOf != product.amountOf) return false;
        if (status != product.status) return false;
        if (Image != null ? !Image.equals(product.Image) : product.Image != null) return false;
        if (amount != null ? !amount.equals(product.amount) : product.amount != null) return false;
        if (categorize != null ? !categorize.equals(product.categorize) : product.categorize != null)
            return false;
        if (code != null ? !code.equals(product.code) : product.code != null) return false;
        if (description != null ? !description.equals(product.description) : product.description != null)
            return false;
        if (distant != null ? !distant.equals(product.distant) : product.distant != null)
            return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (netweight != null ? !netweight.equals(product.netweight) : product.netweight != null)
            return false;
        if (price != null ? !price.equals(product.price) : product.price != null) return false;
        if (promoprice != null ? !promoprice.equals(product.promoprice) : product.promoprice != null)
            return false;
        if (shelf != null ? !shelf.equals(product.shelf) : product.shelf != null) return false;
        if (zone != null ? !zone.equals(product.zone) : product.zone != null) return false;

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
        result = 31 * result + (zone != null ? zone.hashCode() : 0);
        result = 31 * result + (shelf != null ? shelf.hashCode() : 0);
        result = 31 * result + (status ? 1 : 0);
        result = 31 * result + (Image != null ? Image.hashCode() : 0);
        result = 31 * result + (distant != null ? distant.hashCode() : 0);
        return result;
    }

}
