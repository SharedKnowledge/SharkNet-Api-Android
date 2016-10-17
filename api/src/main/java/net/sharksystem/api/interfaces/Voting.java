package net.sharksystem.api.interfaces;

import java.util.HashMap;
import java.util.List;

/**
 * Created by timol on 27.06.2016.
 */
public interface Voting {

	/**
	 * Returns the Question of the Voting
	 * @return
     */
	String getQuestion();

	/**
	 * Returns if the Voting is Single choice (only one Answer per Person possible)
	 * No evaluation, this has to be done in the gui
	 * @return
     */
	boolean isSingleChoice();

	/**
	 * Adds the List of Answers to the voting
	 * @param answers
     */
	void addAnswers(List<String> answers);

	/**
	 * Returns the Answers including the votes as a hashMap
	 * @return
     */
	HashMap<String, List<Contact>> getVotings();

	/**
	 * Returns the Answers in a HashMap, Contact can be added to the answers to vote
	 * @return
     */
	HashMap<String, Contact> getAnswers();

	/**
	 * add the contact to the answers they voted for
	 * @param votes
     */
	boolean vote(HashMap<String, Contact> votes);

	/**
	 * Evaluates if the Contact already has voted
	 * @param c
	 * @return
     */
	boolean alreadyVoted(Contact c);

	/**
	 * Saves the Voting in the Database
	 */
	void save();

	/**
	 * Deletes the Voting from the Database
	 */
	void delete();


}
