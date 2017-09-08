package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.users.CollaborationMember;

public class AccessRightUtils {

    public static CollaborationMember checkMemebrAccess(Collaboration collaboration, String username){
        CollaborationMember user = null;
        for (CollaborationMember memeber: ((Project) collaboration).getAllMembers()) {
            if(memeber.getUsername().equals(username))
               user = memeber;
        }
        return user;
    }

    public static boolean checkAccessRight(CollaborationMember user){
        if(user.getAccessRight().equals(AccessRight.READ))
            return false;
        else
            return true;
    }
}
