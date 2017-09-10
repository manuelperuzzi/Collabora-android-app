package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.users.CollaborationMember;

/**
 * Implement some method to check access rights of a member
 */
public class AccessRightUtils {

    /**
     * Method that take the CollaborationMember object of the user that is logged in the application
     * @param collaboration the collaboration in which you are
     * @param username the username of the logged user
     * @return return the CollaborationMember
     */
    public static CollaborationMember checkMemberAccess(Collaboration collaboration, String username){
        CollaborationMember user = null;
        for (CollaborationMember member: ((SharedCollaboration) collaboration).getAllMembers()) {
            if(member.getUsername().equals(username))
               user = member;
        }
        return user;
    }

    /**
     * check if the logged user has the access right to create or update a thing
     * @param user the Collaboration user to check
     * @return return true if he has ADMIN or WRITE accessRight
     */
    public static boolean checkIfUserHasAccessRight(CollaborationMember user){
        return !user.getAccessRight().equals(AccessRight.READ);
    }
}
