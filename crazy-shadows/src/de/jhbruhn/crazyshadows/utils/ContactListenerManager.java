package de.jhbruhn.crazyshadows.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactListenerManager implements ContactListener {

	private List<ContactListener> listeners = new ArrayList<ContactListener>();

	public List<ContactListener> getListeners() {
		return listeners;
	}

	@Override
	public void beginContact(Contact contact) {
		for (ContactListener l : listeners)
			l.beginContact(contact);
	}

	@Override
	public void endContact(Contact contact) {
		for (ContactListener l : listeners)
			l.endContact(contact);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		for (ContactListener l : listeners)
			l.preSolve(contact, oldManifold);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		for (ContactListener l : listeners)
			l.postSolve(contact, impulse);
	}

}
