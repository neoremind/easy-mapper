package com.baidu.unbiz.easymapper.vo;

/**
 * @author zhangxu
 */
public class Address2 {

    private String street;

    private String no;

    public Address2(String street, String no) {
        this.street = street;
        this.no = no;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", no='" + no + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address2 address = (Address2) o;

        if (street != null ? !street.equals(address.street) : address.street != null) {
            return false;
        }
        return !(no != null ? !no.equals(address.no) : address.no != null);

    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (no != null ? no.hashCode() : 0);
        return result;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
