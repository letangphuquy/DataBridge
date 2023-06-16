package Rules;

/**
 * ServerCode
 * Rules for communication from server to client (via sockets) (aka. responses)
 * Code + <space> + Requester_User_ID + <space> +  Details
 * Special cases: 
 * - "KEY" + <space> + Public_Key
 * - "ACCEPT" + <space> + [User information (JSON)] (on login success)
 * MAJOR UPDATE:
 * ServerCode will be symmetrical to ClientCode
 */

public enum ServerCode {
    KEY,
    ACCEPT, REJECT, DATA, ERROR
}
