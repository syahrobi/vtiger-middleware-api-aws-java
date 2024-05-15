package vtiger.middleware.api.service.impl;

import vtiger.middleware.api.config.RestTemplateConfig;
import vtiger.middleware.api.config.VtigerProperties;
import co.teraglobal.vtiger.middleware.api.dto.*;
import vtiger.middleware.api.dto.*;
import vtiger.middleware.api.model.ElementType;
import vtiger.middleware.api.model.Operation;
import vtiger.middleware.api.model.TicketPriority;
import vtiger.middleware.api.model.TicketStatus;
import vtiger.middleware.api.service.VtigerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class VtigerServiceImpl implements VtigerService {

    @Autowired
    private RestTemplateConfig restTemplateConfig;

    @Autowired
    private VtigerProperties vtigerProperties;

    @Override
    public TicketResponseDto create(TicketRequestDto ticketRequestDto) {
        ContactVtigerResponseDto contactVtigerResponseDto;
        TicketResponseDto ticketResponseDto = new TicketResponseDto();
        try {
            if (ticketRequestDto.getContactId().isEmpty() || ticketRequestDto.getContactId() == null) {
                contactVtigerResponseDto = createContact(ticketRequestDto);
                log.info("create contact : {}", contactVtigerResponseDto);
                ticketRequestDto.setContactId(contactVtigerResponseDto.getResult().getId());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("operation", Operation.create.toString());
            params.add("sessionName", resetSession());
            params.add("element", "{\n" +
                    "    \"assigned_user_id\": \""+ ticketRequestDto.getAssignToId() +"\",\n" +
                    "    \"ticketpriorities\": \""+ TicketPriority.Low +"\",\n" +
                    "    \"ticketstatus\": \""+ TicketStatus.Open +"\",\n" +
                    "    \"ticket_title\": \""+ ticketRequestDto.getTitle() +"\",\n" +
                    "    \"contact_id\":\""+ ticketRequestDto.getContactId() +"\"\n" +
                    "}");
            params.add("elementType", ElementType.HelpDesk.toString());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            TicketVtigerResponseDto ticketVtigerResponseDto = restTemplateConfig.restTemplate()
                    .postForEntity(vtigerProperties.getUrl(),
                            request ,
                            TicketVtigerResponseDto.class )
                    .getBody();
            log.info("create ticket : {}", ticketVtigerResponseDto);

            if (!ticketVtigerResponseDto.isSuccess()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ticketVtigerResponseDto.getResult().toString());
            }

            ticketResponseDto.setContactId(ticketVtigerResponseDto.getResult().getContact_id());
            ticketResponseDto.setTicketId(ticketVtigerResponseDto.getResult().getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ticketResponseDto;
    }

    @Override
    public UserListResponseDto getUserList() {
        UserListResponseDto userListResponseDto;
        try {
            userListResponseDto = restTemplateConfig.restTemplate().getForObject("http://192.168.6.16/webservice.php?sessionName="+ resetSession() +"&operation=query&query=SELECT id, user_name FROM Users;", UserListResponseDto.class);
            if (!userListResponseDto.isSuccess()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userListResponseDto.getResult().toString());
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return userListResponseDto;
    }

    @Override
    public String getToken() {
        String token = "";
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            GetChallengeResponseDto getChallengeResponseDto = restTemplateConfig.restTemplate()
                    .exchange(
                            vtigerProperties.getUrl() + "?operation="+ Operation.getchallenge +"&username=" + vtigerProperties.getUsername(),
                            HttpMethod.GET,
                            entity,
                            GetChallengeResponseDto.class
                    ).getBody();
            if (!getChallengeResponseDto.isSuccess()) {
                log.error("get token failed");
            }
            token = getChallengeResponseDto.getResult().getToken();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return token;
    }

    @Override
    public String resetSession() {
        String session = "";
        try {
            String token;

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            GetChallengeResponseDto getChallengeResponseDto = restTemplateConfig.restTemplate()
                    .exchange(
                            vtigerProperties.getUrl() + "?operation="+ Operation.getchallenge +"&username=" + vtigerProperties.getUsername(),
                            HttpMethod.GET,
                            entity,
                            GetChallengeResponseDto.class
                    ).getBody();
            if (!getChallengeResponseDto.isSuccess()) {
                log.error("get token failed");
            }
            token = getChallengeResponseDto.getResult().getToken();

            LoginVtigerRequestDto loginVtigerRequestDto = new LoginVtigerRequestDto();
            loginVtigerRequestDto.setUsername(vtigerProperties.getUsername());
            loginVtigerRequestDto.setAccessKey(md5Generator(token + vtigerProperties.getAdminAccessKey()));
            session = getSession(loginVtigerRequestDto).getResult().getSessionName();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return session;
    }

    private ContactVtigerResponseDto createContact(TicketRequestDto ticketRequestDto) {
        ContactVtigerResponseDto contactVtigerResponseDto = new ContactVtigerResponseDto();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("operation", Operation.create.toString());
            params.add("sessionName", resetSession());
            params.add("element", "{" +
                    "    \"firstname\":\""+ ticketRequestDto.getFirstname() +"\",\n" +
                    "    \"lastname\":\""+ ticketRequestDto.getLastname() +"\",\n" +
                    "    \"assigned_user_id\": \""+ ticketRequestDto.getAssignToId() +"\",\n" +
                    "    \"mobile\": \""+ ticketRequestDto.getPhone() +"\",\n" +
                    "    \"email\": \""+ ticketRequestDto.getEmail() +"\"\n" +
                    "}");
            params.add("elementType", ElementType.Contacts.toString());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            contactVtigerResponseDto = restTemplateConfig.restTemplate()
                    .postForEntity(
                            vtigerProperties.getUrl(),
                            request,
                            ContactVtigerResponseDto.class
                    ).getBody();
            if (!contactVtigerResponseDto.isSuccess()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return contactVtigerResponseDto;
    }

    private LoginVtigerResponseDto getSession(LoginVtigerRequestDto loginVtigerRequestDto) {
        LoginVtigerResponseDto loginVtigerResponseDto = new LoginVtigerResponseDto();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("operation", Operation.login.toString());
            map.add("username", loginVtigerRequestDto.getUsername());
            map.add("accessKey", loginVtigerRequestDto.getAccessKey());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            loginVtigerResponseDto = restTemplateConfig.restTemplate()
                    .postForEntity(
                            vtigerProperties.getUrl(),
                            request,
                            LoginVtigerResponseDto.class
                    ).getBody();
            if (!loginVtigerResponseDto.isSuccess()) {
                log.error("Login failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginVtigerResponseDto;
    }

    private String md5Generator(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            System.out.println("generated " + hashtext);
            return hashtext;
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
