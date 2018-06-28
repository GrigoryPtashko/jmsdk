/*
 * Copyright sablintolya@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ma1uta.matrix.client.methods;

import io.github.ma1uta.matrix.Event;
import io.github.ma1uta.matrix.Page;
import io.github.ma1uta.matrix.client.MatrixClient;
import io.github.ma1uta.matrix.client.api.EventApi;
import io.github.ma1uta.matrix.client.model.event.JoinedMembersResponse;
import io.github.ma1uta.matrix.client.model.event.MembersResponse;
import io.github.ma1uta.matrix.client.model.event.RedactRequest;
import io.github.ma1uta.matrix.client.model.event.SendEventResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ws.rs.core.GenericType;

/**
 * EventMethods api.
 */
public class EventMethods {

    private final MatrixClient matrixClient;

    public EventMethods(MatrixClient matrixClient) {
        this.matrixClient = matrixClient;
    }

    public MatrixClient getMatrixClient() {
        return matrixClient;
    }

    /**
     * Get a single event based on roomId/eventId. You must have permission to retrieve this event e.g. by being a member in the
     * room for this event.
     *
     * @param roomId  The ID of the room the event is in.
     * @param eventId The event ID to get.
     * @return The full event.
     */
    public Event event(String roomId, String eventId) {
        Objects.requireNonNull(roomId, "RoomId cannot be empty.");
        Objects.requireNonNull(eventId, "EventId cannot be empty.");
        RequestParams params = new RequestParams().pathParam("roomId", roomId).pathParam("eventId", eventId);
        return getMatrixClient().getRequestMethods().get(EventApi.class, "singleEvent", params, Event.class);
    }

    /**
     * Looks up the contents of a state event in a room. If the user is joined to the room then the state is taken from the current
     * state of the room. If the user has left the room then the state is taken from the state of the room when they left.
     *
     * @param roomId    The room to look up the state in.
     * @param eventType The type of state to look up.
     * @param stateKey  The key of the state to look up.
     * @return The content of the state event.
     */
    public Map<String, Object> eventContent(String roomId, String eventType, String stateKey) {
        Objects.requireNonNull(roomId, "RoomId cannot be empty.");
        Objects.requireNonNull(eventType, "EventType cannot be empty.");
        Objects.requireNonNull(stateKey, "StateKey cannot be empty.");
        RequestParams params = new RequestParams().pathParam("roomId", roomId).pathParam("eventType", eventType)
             .pathParam("stateKey", stateKey);
        return getMatrixClient().getRequestMethods()
            .get(EventApi.class, "eventsForRoomWithTypeAndState", params, new GenericType<Map<String, Object>>() {
            });
    }

    /**
     * Looks up the contents of a state event in a room. If the user is joined to the room then the state is taken from the current
     * state of the room. If the user has left the room then the state is taken from the state of the room when they left.
     *
     * @param roomId    The room to look up the state in.
     * @param eventType The type of state to look up.
     * @return The content of the state event.
     */
    public Map<String, Object> eventContent(String roomId, String eventType) {
        Objects.requireNonNull(roomId, "RoomId cannot be empty.");
        Objects.requireNonNull(eventType, "EventType cannot be empty.");
        RequestParams params = new RequestParams().pathParam("roomId", roomId).pathParam("eventType", eventType);
        return getMatrixClient().getRequestMethods()
            .get(EventApi.class, "eventsForRoomWithType", params, new GenericType<Map<String, Object>>() {
            });
    }

    /**
     * Get the state events for the current state of a room.
     *
     * @param roomId The room to look up the state for.
     * @return The current state of the room.
     */
    public List<Event> events(String roomId) {
        Objects.requireNonNull(roomId, "RoomId cannot be empty.");
        RequestParams params = new RequestParams().pathParam("roomId", roomId);
        return getMatrixClient().getRequestMethods().get(EventApi.class, "eventForRoom", params, new GenericType<List<Event>>() {
        });
    }

    /**
     * Get the list of members for this room.
     *
     * @param roomId The room to get the member events for.
     * @return A list of members of the room.
     */
    public MembersResponse members(String roomId) {
        Objects.requireNonNull(roomId, "RoomId cannot be empty.");
        RequestParams params = new RequestParams().pathParam("roomId", roomId);
        return getMatrixClient().getRequestMethods().get(EventApi.class, "members", params, MembersResponse.class);
    }

    /**
     * This API returns a map of MXIDs to member info objects for members of the room.
     *
     * @param roomId The room to get the members of.
     * @return A map of MXID to room member objects.
     */
    public JoinedMembersResponse joinedMembers(String roomId) {
        Objects.requireNonNull(roomId, "RoomId cannot be empty.");
        RequestParams params = new RequestParams().pathParam("roomId", roomId);
        return getMatrixClient().getRequestMethods().get(EventApi.class, "joinedMembers", params, JoinedMembersResponse.class);
    }

    /**
     * This API returns a list of message and state events for a room. It uses pagination query parameters to paginate history in the room.
     *
     * @param roomId The room to get events from.
     * @param from   The token to start returning events from.
     * @param to     The token to stop returning events at.
     * @param dir    The direction to return events from.
     * @param limit  The direction to return events from.
     * @param filter A JSON RoomEventFilter to filter returned events with.
     * @return A list of messages with a new token to request more.
     */
    public Page<Event> messages(String roomId, String from, String to, String dir, Integer limit, String filter) {
        Objects.requireNonNull(roomId, "RoomId cannot be empty.");
        Objects.requireNonNull(from, "From cannot be empty.");
        RequestParams params = new RequestParams().pathParam("roomId", roomId)
            .queryParam("from", from)
            .queryParam("to", to)
            .queryParam("dir", dir)
            .queryParam("filter", filter);
        if (limit != null) {
            params.queryParam("limit", Integer.toString(limit));
        }
        return getMatrixClient().getRequestMethods().get(EventApi.class, "messages", params, new GenericType<Page<Event>>() {
        });
    }

    /**
     * State events can be sent using this endpoint. These events will be overwritten if (room id), (event type) and (state key) all match.
     *
     * @param roomId       The room to set the state in.
     * @param eventType    The type of event to send.
     * @param stateKey     The state_key for the state to send. Defaults to the empty string.
     * @param eventContent Event content.
     * @return An ID for the sent event.
     */
    public String sendStateEvent(String roomId, String eventType, String stateKey, Map<String, Object> eventContent) {
        Objects.requireNonNull(roomId, "RoomId cannot be empty.");
        Objects.requireNonNull(eventType, "EventType cannot be empty.");
        Objects.requireNonNull(stateKey, "StateKey cannot be empty.");
        RequestParams params = new RequestParams().pathParam("roomId", roomId).pathParam("eventType", eventType)
             .pathParam("stateKey", stateKey);
        return getMatrixClient().getRequestMethods()
            .put(EventApi.class, "sendEventWithTypeAndState", params, eventContent, SendEventResponse.class).getEventId();
    }

    /**
     * State events can be sent using this endpoint. These events will be overwritten if (room id), (event type) and (state key) all match.
     *
     * @param roomId       The room to set the state in.
     * @param eventType    The type of event to send.
     * @param eventContent Event content.
     * @return An ID for the sent event.
     */
    public String sendStateEvent(String roomId, String eventType, Map<String, Object> eventContent) {
        Objects.requireNonNull(roomId, "RoomId cannot be empty.");
        Objects.requireNonNull(eventType, "EventType cannot be empty.");
        RequestParams params = new RequestParams().pathParam("roomId", roomId).pathParam("eventType", eventType);
        return getMatrixClient().getRequestMethods().put(EventApi.class, "sendEventWithType", params, eventContent, SendEventResponse.class)
            .getEventId();
    }

    /**
     * This endpoint is used to send a message event to a room. Message events allow access to historical events and pagination,
     * making them suited for "once-off" activity in a room.
     *
     * @param roomId       The room to send the event to.
     * @param eventType    The type of event to send.
     * @param eventContent Event content.
     * @return An ID for the sent event.
     */
    public String sendEvent(String roomId, String eventType, Map<String, Object> eventContent) {
        Objects.requireNonNull(roomId, "RoomId cannot be empty.");
        Objects.requireNonNull(eventType, "EventType cannot be empty.");
        RequestParams params = new RequestParams().pathParam("roomId", roomId).pathParam("eventType", eventType)
            .pathParam("txnId", Long.toString(System.currentTimeMillis()));
        return getMatrixClient().getRequestMethods().put(EventApi.class, "sendEvent", params, eventContent, SendEventResponse.class)
            .getEventId();
    }

    /**
     * Strips all information out of an event which isn't critical to the integrity of the server-side representation of the room.
     *
     * @param roomId  The room from which to redact the event.
     * @param eventId The ID of the event to redact.
     * @param reason  The reason for the event being redacted.
     * @return An ID for the redaction event.
     */
    public String redact(String roomId, String eventId, String reason) {
        Objects.requireNonNull(roomId, "RoomId cannot be empty.");
        Objects.requireNonNull(eventId, "EventId cannot be empty.");
        RequestParams params = new RequestParams().pathParam("roomId", roomId).pathParam("eventId", eventId)
            .pathParam("txnId", Long.toString(System.currentTimeMillis()));
        RedactRequest request = new RedactRequest();
        request.setReason(reason);
        return getMatrixClient().getRequestMethods().put(EventApi.class, "redact", params, request, SendEventResponse.class).getEventId();
    }

    /**
     * Send message.
     *
     * @param roomId      room id.
     * @param text        message.
     * @param messageType message type.
     * @return sent event id.
     */
    public String sendMessage(String roomId, String text, String messageType) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("msgtype", messageType);
        payload.put("body", text);

        return sendEvent(roomId, Event.EventType.ROOM_MESSAGE, payload);
    }

    /**
     * Send message.
     *
     * @param roomId room id.
     * @param text   message.
     * @return sent event id.
     */
    public String sendMessage(String roomId, String text) {
        return sendMessage(roomId, text, Event.MessageType.TEXT);
    }

    /**
     * Send notice.
     *
     * @param roomId room id.
     * @param text   message.
     * @return sent event id.
     */
    public String sendNotice(String roomId, String text) {
        return sendMessage(roomId, text, Event.MessageType.NOTICE);
    }

    /**
     * Send formatted message.
     *
     * @param roomId        room id.
     * @param text          message.
     * @param formattedText formatted message.
     * @return sent event id.
     */
    public SendEventResponse sendFormattedMessage(String roomId, String text, String formattedText) {
        Map<String, String> payload = new HashMap<>();
        payload.put("msgtype", Event.MessageType.TEXT);
        payload.put("body", text);
        payload.put("formatted_body", formattedText);
        payload.put("format", "org.matrix.custom.html");

        RequestParams params = new RequestParams().pathParam("roomId", roomId).pathParam("eventType", Event.EventType.ROOM_MESSAGE)
            .pathParam("txnId", Long.toString(System.currentTimeMillis()));
        return getMatrixClient().getRequestMethods().put(EventApi.class, "sendEvent", params, payload, SendEventResponse.class);
    }

    /**
     * Send notice.
     *
     * @param roomId        room id.
     * @param text          message.
     * @param formattedText formatted message.
     * @return sent event id.
     */
    public SendEventResponse sendFormattedNotice(String roomId, String text, String formattedText) {
        Map<String, String> payload = new HashMap<>();
        payload.put("msgtype", Event.MessageType.NOTICE);
        payload.put("body", text);
        payload.put("formatted_body", formattedText);
        payload.put("format", "org.matrix.custom.html");

        RequestParams params = new RequestParams().pathParam("roomId", roomId).pathParam("eventType", Event.EventType.ROOM_MESSAGE)
            .pathParam("txnId", Long.toString(System.currentTimeMillis()));
        return getMatrixClient().getRequestMethods().put(EventApi.class, "sendEvent", params, payload, SendEventResponse.class);
    }
}