package com.cube9.afary.user.home_services.helperClass;

public class HomeServicesPojo {
    String service_name,service_id,service_image,serives_availability,description;

    public HomeServicesPojo(String service_name, String service_id, String service_image, String serives_availability,String description) {
        this.service_name = service_name;
        this.service_id = service_id;
        this.service_image = service_image;
        this.serives_availability = serives_availability;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_image() {
        return service_image;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
    }

    public String getSerives_availability() {
        return serives_availability;
    }

    public void setSerives_availability(String serives_availability) {
        this.serives_availability = serives_availability;
    }
}
