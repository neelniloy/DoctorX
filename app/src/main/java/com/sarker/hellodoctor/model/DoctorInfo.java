package com.sarker.hellodoctor.model;

public class DoctorInfo {
    public String doctorName,doctorImage,doctorDegree;

    public DoctorInfo(){

    }

    public DoctorInfo(String doctorName, String doctorImage, String doctorDegree) {
        this.doctorName = doctorName;
        this.doctorImage = doctorImage;
        this.doctorDegree = doctorDegree;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorImage() {
        return doctorImage;
    }

    public void setDoctorImage(String doctorImage) {
        this.doctorImage = doctorImage;
    }

    public String getDoctorDegree() {
        return doctorDegree;
    }

    public void setDoctorDegree(String doctorDegree) {
        this.doctorDegree = doctorDegree;
    }
}
