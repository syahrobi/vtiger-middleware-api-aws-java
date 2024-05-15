package vtiger.middleware.api.dto;

import lombok.Data;

@Data
public class TicketResultDto {
    private String ticket_no;
    private String assigned_user_id;
    private String parent_id;
    private String ticketpriorities;
    private String product_id;
    private String ticketseverities;
    private String ticketstatus;
    private String ticketcategories;
    private String update_log;
    private String hours;
    private String days;
    private String createdtime;
    private String modifiedtime;
    private String from_portal;
    private String modifiedby;
    private String ticket_title;
    private String description;
    private String solution;
    private String contact_id;
    private String source;
    private String starred;
    private String tags;
    private String id;
    private String label;
}
