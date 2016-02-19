package com.headhunt.utils.commonutils.tokenizerutils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shekhar2010us
 */
public class TokenizerActionSelector {

    private boolean cleanHtml = false;
    private boolean convertToLowerCase = false;
    private boolean removeURLs = false;
    private boolean removePunct = false;
    private int[] charThresholds = {0,100000};
    private boolean removeNumbers = false;
    private boolean removeStopWords = false;
    private boolean removeBlacklist = false;
    private boolean modifyAcronyms = false;
    private boolean performStemming = false;
    private String tokenizeDelimiter = " ";
    private int ngrams = 1;
    private int[] whichGrams;
    private int minimumTokenLength = 0;
    private boolean removePhoneNumber = false;
    private boolean removeHomeAddress = false;
    private boolean removeEmailAddress = false;
    private List<String> extraRegexToRemove = new ArrayList<>();

    /**
     * @return the clean Html
     */
    public boolean isCleanHtml() {
        return cleanHtml;
    }

    /**
     * @param cleanHtml the cleanHtml to set
     */
    public TokenizerActionSelector setCleanHtml(boolean cleanHtml) {
        this.cleanHtml = cleanHtml;
        return this;
    }

    /**
     * @return the convertToLowerCase
     */
    public boolean isConvertToLowerCase() {
        return convertToLowerCase;
    }

    /**
     * @param convertToLowerCase the convertToLowerCase to set
     */
    public TokenizerActionSelector setConvertToLowerCase(boolean convertToLowerCase) {
        this.convertToLowerCase = convertToLowerCase;
        return this;
    }

    /**
     * @return the removeURLs
     */
    public boolean isRemoveURLs() {
        return removeURLs;
    }

    /**
     * @param removeURLs the removeURLs to set
     */
    public TokenizerActionSelector setRemoveURLs(boolean removeURLs) {
        this.removeURLs = removeURLs;
        return this;
    }

    /**
     * @return the removePunct
     */
    public boolean isRemovePunct() {
        return removePunct;
    }

    /**
     * @param removePunct the removePunct to set
     */
    public TokenizerActionSelector setRemovePunct(boolean removePunct) {
        this.removePunct = removePunct;
        return this;
    }

    /**
     * @return the charThresholds
     */
    public int[] getCharThresholds() {
        return charThresholds;
    }

    /**
     * @param charThresholds the charThresholds to set
     */
    public TokenizerActionSelector setCharThresholds(int[] charThresholds) {
        this.charThresholds = charThresholds;
        return this;
    }

    /**
     * @return the removeNumbers
     */
    public boolean isRemoveNumbers() {
        return removeNumbers;
    }

    /**
     * @param removeNumbers the removeNumbers to set
     */
    public TokenizerActionSelector setRemoveNumbers(boolean removeNumbers) {
        this.removeNumbers = removeNumbers;
        return this;
    }

    /**
     * @return the removeStopWords
     */
    public boolean isRemoveStopWords() {
        return removeStopWords;
    }

    public boolean isRemoveBlacklist() {
        return removeBlacklist;
    }

    /**
     * @param removeStopWords the removeStopWords to set
     */
    public TokenizerActionSelector setRemoveStopWords(boolean removeStopWords) {
        this.removeStopWords = removeStopWords;
        return this;
    }

    public TokenizerActionSelector setRemoveBlacklist(boolean removeBlacklist) {
        this.removeBlacklist = removeBlacklist;
        return this;
    }

    /**
     * @return the performStemming
     */
    public boolean isPerformStemming() {
        return performStemming;
    }

    /**
     * @param performStemming the performStemming to set
     */
    public TokenizerActionSelector setPerformStemming(boolean performStemming) {
        this.performStemming = performStemming;
        return this;
    }

    /**
     * @return the tokenizeDelimiter
     */
    public String getTokenizeDelimiter() {
        return tokenizeDelimiter;
    }

    /**
     * @param tokenizeDelimiter the tokenizeDelimiter to set
     */
    public TokenizerActionSelector setTokenizeDelimiter(String tokenizeDelimiter) {
        this.tokenizeDelimiter = tokenizeDelimiter;
        return this;
    }

    /**
     * @return the ngrams
     */
    public int getNgrams() {
        return ngrams;
    }

    /**
     * @param ngrams the ngrams to set
     */
    public TokenizerActionSelector setNgrams(int ngrams) {
        this.ngrams = ngrams;
        return this;
    }

    /**
     * @return the minimumTokenLength
     */
    public int getMinimumTokenLength() {
        return minimumTokenLength;
    }

    /**
     * @param minimumTokenLength the ngrams to set
     */
    public TokenizerActionSelector setMinimumTokenLength(int minimumTokenLength) {
        this.minimumTokenLength = minimumTokenLength;
        return this;
    }


    public int[] getWhichGrams() {
        return whichGrams;
    }

    public void setWhichGrams(int[] whichGrams) {
        this.whichGrams = whichGrams;
    }

    public boolean isRemovePhoneNumber() {
        return removePhoneNumber;
    }

    public void setRemovePhoneNumber(boolean removePhoneNumber) {
        this.removePhoneNumber = removePhoneNumber;
    }

    public boolean isRemoveHomeAddress() {
        return removeHomeAddress;
    }

    public void setRemoveHomeAddress(boolean removeHomeAddress) {
        this.removeHomeAddress = removeHomeAddress;
    }

    public boolean isRemoveEmailAddress() {
        return removeEmailAddress;
    }

    public void setRemoveEmailAddress(boolean removeEmailAddress) {
        this.removeEmailAddress = removeEmailAddress;
    }

    public List<String> getExtraRegexToRemove() {
        return extraRegexToRemove;
    }

    public void setExtraRegexToRemove(List<String> extraRegexToRemove) {
        this.extraRegexToRemove = extraRegexToRemove;
    }

    public boolean isModifyAcronyms() {
        return modifyAcronyms;
    }

    public void setModifyAcronyms(boolean modifyAcronyms) {
        this.modifyAcronyms = modifyAcronyms;
    }
}
