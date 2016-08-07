package com.baidu.unbiz.easymapper.pojo;

/**
 * @author zhangxu
 */
public class Address {

    private String street;

    private int no;

    public Address(String street, int no) {
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

        Address address = (Address) o;

        if (no != address.no) {
            return false;
        }
        return !(street != null ? !street.equals(address.street) : address.street != null);

    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + no;
        return result;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
