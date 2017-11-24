package entities;

import javax.persistence.*;
import java.util.Random;
import java.util.Set;

@Entity
public class Customers {

    @Id
    @Column(name = "customerid")
    private String customerid;

    private String companyname;
    private String contactname;
    private String contacttitle;
    private String address;
    private String city;
    private String region;
    private String postalcode;
    private String country;
    private String phone;
    private String fax;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Orders> orders;

    public Customers() {
    }

    public Customers(String customerid, String companyname, String contactname, String contacttitle, String address,
                     String city, String region, String postalcode, String country, String phone, String fax) {
        this.customerid = customerid;
        this.companyname = companyname;
        this.contactname = contactname;
        this.contacttitle = contacttitle;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalcode = postalcode;
        this.country = country;
        this.phone = phone;
        this.fax = fax;
    }

    public Set<Orders> getOrders() {
        return orders;
    }

    public void setOrders(Set<Orders> orders) {
        this.orders = orders;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContacttitle() {
        return contacttitle;
    }

    public void setContacttitle(String contacttitle) {
        this.contacttitle = contacttitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public boolean generateCustomerId(){
        if ((companyname != null) ){
            String name = companyname.replace(" ", "");
            customerid = name.substring(0,1).toUpperCase();
            char[] chars = name.substring(1).toUpperCase().toCharArray();
            Random rand = new Random();
            int i = 1;
            while (i < 5){
                customerid += chars[rand.nextInt(chars.length)];
                i++;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "customerid='" + customerid + '\'' +
                ", companyname='" + companyname + '\'' +
                ", contactname='" + contactname + '\'' +
                ", contacttitle='" + contacttitle + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", postalcode='" + postalcode + '\'' +
                ", country='" + country + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                '}';
    }

    public String[] toArray() {
        String[] fields = {customerid, companyname, contactname, contacttitle, address, city, region, postalcode,
                country, phone, fax};
        return fields;
    }
}
