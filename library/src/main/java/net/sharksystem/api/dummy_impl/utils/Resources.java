package net.sharksystem.api.dummy_impl.utils;

import net.sharksystem.api.interfaces.SharkNet;

import java.io.File;
import java.net.URL;

/**
 * @author Yves Kaufmann
 * @since 17.07.2016
 */
public class Resources {
	public static File get(String name) {
		final URL recourseURL = SharkNet.class.getResource(name);
		if (recourseURL == null) {
			throw new RuntimeException("Resource not found" + name);
		}

		return null; //Paths.get(recourseURL.toURI()).toFile(); TODO rework for Android
	}
}
