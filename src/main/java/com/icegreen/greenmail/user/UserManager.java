/*
 * Copyright (c) 2006 Wael Chatila / Icegreen Technologies. All Rights Reserved.
 * This software is released under the LGPL which is available at http://www.gnu.org/copyleft/lesser.html
 * This file has been used and modified. Original file can be found on http://foedus.sourceforge.net
 */
package com.icegreen.greenmail.user;

import com.icegreen.greenmail.imap.ImapHostManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class UserManager {
    Map<String, GreenMailUser> _users = Collections.synchronizedMap(new HashMap<String, GreenMailUser>());
    private ImapHostManager imapHostManager;

    public UserManager(ImapHostManager imapHostManager) {
        this.imapHostManager = imapHostManager;
    }

    public GreenMailUser getUser(String login) {
        return _users.get(login);
    }

    public GreenMailUser getUserByEmail(String email) {
        GreenMailUser ret = getUser(email);
        if (null == ret) {
            for (Iterator<GreenMailUser> it = _users.values().iterator(); it.hasNext();) {
                GreenMailUser u = it.next();
                if (u.getEmail().trim().equalsIgnoreCase(email.trim())) {
                    return u;
                }
            }
        }
        return ret;
    }

    public GreenMailUser createAdmin(String name, String login, String password) throws UserException {
        GreenMailUser user = new UserImpl(name, login, password, true, imapHostManager);
        user.create();
        addUser(user);
        return user;
    }

    public GreenMailUser createUser(String name, String login, String password) throws UserException {
        GreenMailUser user = new UserImpl(name, login, password, false, imapHostManager);
        user.create();
        addUser(user);
        return user;
    }

    private void addUser(GreenMailUser user) {
        _users.put(user.getLogin(), user);
    }

    public void deleteUser(GreenMailUser user)
            throws UserException {
        user = _users.remove(user.getLogin());
        if (user != null)
            user.delete();
    }

    public boolean test(String userid, String password) {
        GreenMailUser u = getUser(userid);
        if (null == u) {
            return false;
        }

        return u.getPassword().equals(password);
    }

    public ImapHostManager getImapHostManager() {
        return imapHostManager;
    }


}