package com.nsretail.data.model.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfficialDetail {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_id")
    @Expose
    public int user_id;
    @SerializedName("res_address")
    @Expose
    public String res_address;
    @SerializedName("designation")
    @Expose
    public String designation;
    @SerializedName("office_email")
    @Expose
    public String office_email;
    @SerializedName("office_contact_no")
    @Expose
    public String office_contact_no;
    @SerializedName("company_name")
    @Expose
    public String company_name;
    @SerializedName("joining_date")
    @Expose
    public String joining_date;
    @SerializedName("office_address")
    @Expose
    public String office_address;
    @SerializedName("employee_id")
    @Expose
    public String employee_id;
    @SerializedName("employment_type")
    @Expose
    public String employment_type;
    @SerializedName("monthly_home_salary")
    @Expose
    public String monthly_home_salary;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("updated_at")
    @Expose
    public String updated_at;
    @SerializedName("is_office_email_varify")
    @Expose
    public int is_office_email_varify;
    @SerializedName("office_email_otp")
    @Expose
    public String office_email_otp;
    @SerializedName("verified_at")
    @Expose
    public String verified_at;
    @SerializedName("salary_date")
    @Expose
    public String salary_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getRes_address() {
        return res_address;
    }

    public void setRes_address(String res_address) {
        this.res_address = res_address;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOffice_email() {
        return office_email;
    }

    public void setOffice_email(String office_email) {
        this.office_email = office_email;
    }

    public String getOffice_contact_no() {
        return office_contact_no;
    }

    public void setOffice_contact_no(String office_contact_no) {
        this.office_contact_no = office_contact_no;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getJoining_date() {
        return joining_date;
    }

    public void setJoining_date(String joining_date) {
        this.joining_date = joining_date;
    }

    public String getOffice_address() {
        return office_address;
    }

    public void setOffice_address(String office_address) {
        this.office_address = office_address;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployment_type() {
        return employment_type;
    }

    public void setEmployment_type(String employment_type) {
        this.employment_type = employment_type;
    }

    public String getMonthly_home_salary() {
        return monthly_home_salary;
    }

    public void setMonthly_home_salary(String monthly_home_salary) {
        this.monthly_home_salary = monthly_home_salary;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getIs_office_email_varify() {
        return is_office_email_varify;
    }

    public void setIs_office_email_varify(int is_office_email_varify) {
        this.is_office_email_varify = is_office_email_varify;
    }

    public String getOffice_email_otp() {
        return office_email_otp;
    }

    public void setOffice_email_otp(String office_email_otp) {
        this.office_email_otp = office_email_otp;
    }

    public String getVerified_at() {
        return verified_at;
    }

    public void setVerified_at(String verified_at) {
        this.verified_at = verified_at;
    }

    public String getSalary_date() {
        return salary_date;
    }

    public void setSalary_date(String salary_date) {
        this.salary_date = salary_date;
    }
}
