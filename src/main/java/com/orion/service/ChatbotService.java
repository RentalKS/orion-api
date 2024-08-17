package com.orion.service;

import com.orion.dto.chatbot.ChatbotRequest;
import com.orion.dto.chatbot.ChatbotResponse;
import com.orion.entity.ChatHistory;
import com.orion.entity.Vehicle;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.repository.ChatHistoryRepository;
import com.orion.repository.VehicleRepository;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatbotService {
    private final VehicleRepository vehicleRepository;
    private final NLPService nlpService;
    private final ChatHistoryRepository chatHistoryRepository;

    public ChatbotResponse processRequest(ChatbotRequest request) {
        ChatbotResponse response = new ChatbotResponse();
        String userQuery = request.getQuery().toLowerCase();

        VehicleStatus vehicleStatus = getVehicleStatus(userQuery);

        if (vehicleStatus != null) {
            response.setResponseText(handleVehicleStatusQuery(vehicleStatus));
        } else if (userQuery.contains("price estimate")) {
            List<CoreEntityMention> entities = nlpService.extractEntities(userQuery);
            response.setResponseText(handlePriceEstimate(entities));
        } else {
            response.setResponseText("Sorry, I don't understand your request.");
        }

        saveChatHistory(request, response);
        response.setSuccess(true);
        return response;
    }

    private static VehicleStatus getVehicleStatus(String userQuery) {
        VehicleStatus vehicleStatus = null;

        if (userQuery.contains("available vehicles")) {
            vehicleStatus = VehicleStatus.AVAILABLE;
        } else if (userQuery.contains("vehicles rented")) {
            vehicleStatus = VehicleStatus.RENTED;
        } else if (userQuery.contains("vehicles under maintenance")) {
            vehicleStatus = VehicleStatus.UNDER_MAINTENANCE;
        } else if (userQuery.contains("vehicles reserved")) {
            vehicleStatus = VehicleStatus.RESERVED;
        } else if (userQuery.contains("vehicles out of service")) {
            vehicleStatus = VehicleStatus.OUT_OF_SERVICE;
        }
        return vehicleStatus;
    }

    private String handleVehicleStatusQuery(VehicleStatus status) {
        Iterable<Vehicle> availableVehicles = vehicleRepository.findByStatus(status);

        if (!availableVehicles.iterator().hasNext()) {
            return "No vehicles are currently available.";
        }
        StringBuilder responseText = new StringBuilder("Here are the available vehicles:\n");

        availableVehicles.forEach(vehicle ->
                responseText.append("- ")
                        .append(vehicle.getModel().getName())
                        .append(" (")
                        .append(vehicle.getRegistrationNumber())
                        .append(")\n"));

        return responseText.toString();
    }

    private String handlePriceEstimate(List<CoreEntityMention> entities){
        Optional<Vehicle> vehicle = vehicleRepository.findVehicleById(1L);
        long rentalDays = 7;
        double price = rentalDays * 50;
        return "The estimated price for renting a " + " " + vehicle.get().getModel() + " for " + rentalDays + " days is $" + price;
    }

      private void saveChatHistory(ChatbotRequest request, ChatbotResponse response) {
        ChatHistory history = new ChatHistory();
        history.setUserId(request.getUserId());
        history.setQuery(request.getQuery());
        history.setResponse(response.getResponseText());
        history.setTimestamp(LocalDateTime.now());

        chatHistoryRepository.save(history);
    }

}
