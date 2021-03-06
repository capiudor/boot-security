package top.capiudor.security.entity;

import java.io.Serializable;

public class Role implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role.id
     *
     * @mbggenerated Tue Dec 31 15:09:44 CST 2019
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role.role_name
     *
     * @mbggenerated Tue Dec 31 15:09:44 CST 2019
     */
    private String roleName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table role
     *
     * @mbggenerated Tue Dec 31 15:09:44 CST 2019
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role.id
     *
     * @return the value of role.id
     *
     * @mbggenerated Tue Dec 31 15:09:44 CST 2019
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role.id
     *
     * @param id the value for role.id
     *
     * @mbggenerated Tue Dec 31 15:09:44 CST 2019
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role.role_name
     *
     * @return the value of role.role_name
     *
     * @mbggenerated Tue Dec 31 15:09:44 CST 2019
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role.role_name
     *
     * @param roleName the value for role.role_name
     *
     * @mbggenerated Tue Dec 31 15:09:44 CST 2019
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }
}