package com.example.ustc.healthreps.model;

public class Doctor {

	/**
	 * @param doctorName
	 * @param departmentName
	 * @param gradeName
	 * @param hospitalName
	 */
	public Doctor(String doctorName, String departmentName, String gradeName,
			String hospitalName) {
		super();
		this.doctorName = doctorName;
		this.departmentName = departmentName;
		this.gradeName = gradeName;
		this.hospitalName = hospitalName;
	}

	private String doctorName;
	private String departmentName;
	private String gradeName;
	private String hospitalName;

	/**
	 * @return the doctorName
	 */
	public String getDoctorName() {
		return doctorName;
	}

	/**
	 * @param doctorName
	 *            the doctorName to set
	 */
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/**
	 * @param departmentName
	 *            the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * @return the gradeName
	 */
	public String getGradeName() {
		return gradeName;
	}

	/**
	 * @param gradeName
	 *            the gradeName to set
	 */
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	/**
	 * @return the hospitalName
	 */
	public String getHospitalName() {
		return hospitalName;
	}

	/**
	 * @param hospitalName
	 *            the hospitalName to set
	 */
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getDoctorName() + "     " + getDepartmentName() + "     " + getGradeName() + "     " + getHospitalName();
	}
}