package Rules;

/**
 * ServerCode
 * Rules for communication from server to client (via sockets) (aka. responses)
 * 
 * Let D be the delimiter
//  * Code + D + Requester_User_ID + D + Details
 * Use cases: 
 * - "KEY" + D + Public_Key
 * - "ACCEPT" + D + [User information (JSON)] (on login success)
 * - "REJECT" + D + [Reason] (on login failure)
 * - "ACCEPT" + D + [Request_ID] (on request success, for example, file upload)
 * MAJOR UPDATE:
 * ServerCode will be symmetrical to ClientCode
 */

public enum ServerCode {
    KEY,
    ACCEPT, REJECT, DATA, ERROR, 
    CHAT
}
