package Rules;

/**
 * ServerCode
 * Rules for communication from server to client (via sockets) (aka. responses)
 * 
 * Let D be the delimiter
//  * Code <_> Requester_User_ID <_> Details
 * Use cases: 
 * Group 1: "Blocking" communication
 * - KEY <_> Public_Key
 * - ACCEPT (on login/ register success)
 * - REJECT <_> [Reason] (on login failure)
 * - DATA <_> [User's data]
 * 
 * Group 2: General / Specific
 * - ACCEPT <_> [Request_ID] (on request success: file upload, ...)
 * - ACCEPT <_> [Request_ID] <_> [Additional details] (on request success: file download, ...)
 * - REJECT <_> [Request_ID] <_> [Details] (on request failure: file upload, ...)
 * - DATA <_> [Request_ID] <_> [Details] (for a specific request: file download, ...)
 * - CHAT <_> [Message object]
 * 
 * MAJOR UPDATE:
 * ServerCode will be (somewhat) symmetrical to ClientCode
 */

public enum ServerCode {
    KEY,
    ACCEPT, REJECT, DATA, 
    CHAT
}
