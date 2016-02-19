/**
 * Write methods for combos for efficiency
 */

package com.headhunt.utils.commonutils.tokenizerutils;

import com.headhunt.utils.commonutils.mlutils.PorterStemmer;
import com.headhunt.utils.commonutils.urlutils.UtilitiesHtmlText;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author shekhar2010us
 */
public class UtilitiesTokenizer {

    public UtilitiesTokenizer() {}

    private Set<String> stopwords = new HashSet<>();
    private Set<String> blacklist = new HashSet<>();
    private Map<String, String> acronyms = new HashMap<>();

    public UtilitiesTokenizer(Set<String> _stopwords) {
        this.stopwords = _stopwords;
    }

    public UtilitiesTokenizer(Set<String> _stopwords, Set<String> _blacklist) {
        this.stopwords = _stopwords;
        this.blacklist = _blacklist;
    }

    public UtilitiesTokenizer(Set<String> _stopwords, Set<String> _blacklist, Map<String, String> _acronyms) {
        this.stopwords = _stopwords;
        this.blacklist = _blacklist;
        this.acronyms = _acronyms;
    }

    public List<String> Tokenize( String text ) {
        TokenizerActionSelector tu = new TokenizerActionSelector();
        return Tokenize(text, tu);
    }

    public String cleanText(String text, TokenizerActionSelector tu) {

        if ( text != null && StringUtils.isNotEmpty(text.trim()) ) {
            text = text.replaceAll("\\s+", " ");
            text = text.trim();
            // get all parameters
            boolean cleanHtml = tu.isCleanHtml();
            boolean convertToLowerCase = tu.isConvertToLowerCase();
            boolean removeURLs = tu.isRemoveURLs();
            boolean modifyAcronyms = tu.isModifyAcronyms();
            boolean removePunct = tu.isRemovePunct();
//            int[] charThresholds = tu.getCharThresholds();
            boolean removeNums = tu.isRemoveNumbers();
            boolean removeStopWords = tu.isRemoveStopWords();
            boolean removeBlackList = tu.isRemoveBlacklist();
            boolean performStemming = tu.isPerformStemming();
            String tokenizeDelimiter = tu.getTokenizeDelimiter();
            int minTokLen = tu.getMinimumTokenLength();
            boolean removePhone = tu.isRemovePhoneNumber();
            boolean removeHome = tu.isRemoveHomeAddress();
            boolean removeEmail = tu.isRemoveEmailAddress();
            List<String> extraRegexToRemove = tu.getExtraRegexToRemove();

            if (removeEmail) text = removeEmailAddress(text);
            if (removePhone) text = removePhoneNumber(text);
            if (removeHome) text = removeHomeAddress(text);
            if (!extraRegexToRemove.isEmpty()) text = removeExtraRegexToRemove(text, extraRegexToRemove);

            // remove html tags
            if (cleanHtml) text = cleanHtml(text);

            // convert to lower case
            if (convertToLowerCase) text = convertToLower(text);

            // modify acronyms
            if (modifyAcronyms) text = modifyTheAcronyms(text, acronyms);

            // remove blacklist
            if (removeBlackList) text = removeBlackList(text, blacklist);

            // remove URLs
            if (removeURLs) text = removeURLs(text);

            // remove punctuations
            if (removePunct) text = removePunct(text);

            // remove all words outside the bound
//      if ( charThresholds[0] != 0 && charThresholds[1] != 1000 )
//        text = sliceCharThreshold(text, charThresholds[0], charThresholds[1]);

            // remove numbers
            if (removeNums) text = removeNums(text);

            // remove stopwords
            if (removeStopWords) text = removeStopWords(text, stopwords);

            // remove small tokens
            if (minTokLen != 0) text = removeSmallTokens(text, minTokLen, tokenizeDelimiter);

            // perform stemming
            if (performStemming) text = performStemming(text);

            text = text.replaceAll("\\s+", " ");
            text = text.replaceAll("(\\.\\s*\\.)+" , ".");
            text = text.trim();
        }
        return text;
    }

    public List<String> Tokenize( String text, TokenizerActionSelector tu) {
        List<String> tokens = new ArrayList<>();

        if ( text != null && StringUtils.isNotEmpty(text) ) {
            text = cleanText(text, tu);

            int ngrams = tu.getNgrams();
            int[] whichGrams = tu.getWhichGrams();
            String tokenizeDelimiter = tu.getTokenizeDelimiter();

            if (whichGrams != null && whichGrams.length > 0) {
                // do it gram wise
                for (int gram : whichGrams) {
                    tokens.addAll( gramToken( text, gram, tokenizeDelimiter ) );
                }
            } else {
                // if ngrams <= 3, then do simple array operation, otherwise do concat operations
                if (ngrams == 1) {
                    tokens = unigram(text, tokenizeDelimiter);
                } else if(ngrams == 2) {
                    tokens = bigram(text, tokenizeDelimiter);
                } else if (ngrams == 3) {
                    tokens = trigram(text, tokenizeDelimiter);
                } else {
                    tokens = unigram(text, tokenizeDelimiter);
                    if (ngrams > 1) {
                        List<String> finalTokens = formNGrams(tokens, ngrams);
                        tokens.clear();
                        tokens.addAll(finalTokens);
                    }
                }
            }
        }
        Iterator iter = tokens.iterator();
        while (iter.hasNext()) {
            String t = iter.next().toString();
            if (t.trim().isEmpty()) {
                iter.remove();
            }
        }
        return tokens;
    }

    private String removeExtraRegexToRemove(String text, List<String> extraRegexToRemove) {
        for (String regex : extraRegexToRemove) {
            Pattern pattern = Pattern.compile(regex , Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                text = text.replace(matcher.group(), " ");
            }
        }
        text = text.replaceAll("( )+", " ").trim();
        return text;
    }

    private String removePhoneNumber(String text) {
        String phone_regex = " \\+?[1-9]{0,3}( )?(\\()?[1-9]{1,11}(\\))?( |-)?[0-9]{1,11}( |-)?[0-9]{1,11}";
        Pattern pattern = Pattern.compile(phone_regex , Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            text = text.replace(matcher.group(), " ");
        }
        text = text.replaceAll("( )+", " ").trim();
        return text;
    }

    private String removeHomeAddress(String text) {
        return text;
    }

    private String removeEmailAddress(String text) {
        String email_regex = " [a-zA-Z1-9\\+\\._]+@[a-zA-Z0-9]{1,15}\\.[a-zA-Z0-9]{1,10}(\\.[A-Za-z0-9]{1,10})?";
        Pattern pattern = Pattern.compile(email_regex , Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            text = text.replace(matcher.group(), " ");
        }
        text = text.replaceAll("( )+", " ").trim();
        return text;
    }

    private List<String> gramToken(String text, int gram, String... delim) {
        List<String> keys = new ArrayList<>();
        text = text.trim();

        String delimiter = " ";
        if ( delim.length != 0 )
            delimiter = delim[0];

        String[] tokens = text.split(delimiter);
        if (tokens.length == gram) {
            keys.add(text.trim());
        } else if (tokens.length > gram) {
            for ( int i = 0 ; i < (tokens.length - gram+1); ++i ) {
                StringBuilder builder = new StringBuilder();
                for (int j = i; j < (i+gram); ++j) {
                    builder.append(tokens[j].trim()).append(delimiter);
                }
                keys.add(builder.toString().trim());
            }
        }
        return keys;
    }

    private List<String> unigram(String text, String ... delim) {
        String delimiter = " ";
        if ( delim.length != 0 )
            delimiter = delim[0];

        String[] tokens = text.split(delimiter);
        List<String> list = new ArrayList<>();
        for (String tok : tokens) {
            list.add(tok.trim());
        }
        return list;
    }

    private List<String> bigram(String text, String ... delim) {
        List<String> unigrams = unigram(text, delim);
        List<String> finallist = new ArrayList<>();
        finallist.addAll(unigrams);

        if (unigrams.size() >= 2) {
            String prev = unigrams.get(0);
            for (int i = 1; i < unigrams.size(); ++i) {
                finallist.add(prev + " " + unigrams.get(i));
                prev = unigrams.get(i);
            }
        }
        return finallist;
    }

    private List<String> trigram(String text, String ... delim) {
        List<String> unigrams = unigram(text, delim);
        List<String> bigrams = bigram(text, delim);

        List<String> finallist = new ArrayList<>();
        finallist.addAll(bigrams);

        if (unigrams.size() >= 3) {
            String first = unigrams.get(0);
            String second = unigrams.get(1);
            for (int i = 2; i < unigrams.size(); ++i) {
                finallist.add(first + " " + second + " " + unigrams.get(i));
                first = second;
                second = unigrams.get(i);
            }
        }

        return finallist;
    }

    private List<String> formNGrams(List<String> tokens, int ngrams) {
        List<String> list = new ArrayList<>();
        list.addAll(tokens);
        for (int j = 2; j <= ngrams; ++j) {
            list.addAll( ngrams( j, tokens ) );
        }
        return list;
    }

    private List<String> ngrams(int n, List<String> words) {
        List<String> ngrams = new ArrayList<String>();
        //String[] words = str.split(" ");
        for (int i = 0; i < words.size() - n + 1; i++)
            ngrams.add(concat(words, i, i+n));
        return ngrams;
    }

    private String concat(List<String> words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append(i > start ? " " : "").append(words.get(i));
        return sb.toString();
    }

    private String removeSmallTokens(String text, int minTokLen, String... delimiter) {
        StringBuilder builder = new StringBuilder();

        String delim = " ";
        if (delimiter != null && delimiter.length > 0) {
            delim = delimiter[0];
        }

        String[] parts = text.split(delim);
        for (String p : parts) {
            if (p.length() > minTokLen) {
                builder.append(p).append(delim);
            }
        }
        return builder.toString().trim();
    }

    private String cleanHtml(String html) {
        // 0 is for using jsoup, 1 is for regex
        return UtilitiesHtmlText.htmlCleanser(html, 0);
    }

    private String convertToLower(String text) {
        return text.toLowerCase();
    }

    private String removeURLs(String text) {
        // (https?:\/\/)?([\da-z\.-]+)\.([a-z:\.]{2,6})([\/\w\.-]*)*\/?

        String regex = "(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z:\\.]{2,6})([\\/\\w\\.-]*)*\\/?";
        text = text.replaceAll(regex, " ");

        return text;
    }

    private String removePunctAndNumbers(String text) {

        text = text.replaceAll("[^a-z ]", " ").replaceAll("\\s+", " ");

        return text;
    }

    private String removePunct(String text) {

        StringBuilder builder = new StringBuilder();
        for(char c : text.toCharArray())
            if (Character.isLetterOrDigit(c))
                builder.append(c);
            else
                builder.append(" ");
        String result = builder.toString();
        result = result.replaceAll("\\s+", " ");
        return result;
    }

    private String removeNums(String text) {

        StringBuilder builder = new StringBuilder();
        for(char c : text.toCharArray())
            if (!Character.isDigit(c))
                builder.append(c);
                //else if ( Character.isSpaceChar(c) )
            else
                builder.append(" ");
        String result = builder.toString();
        result = result.replaceAll("\\s+", " ");
        return result;
    }

    private String performStemming(String text) {
        return PorterStemmer.stemmer(text);
    }

    private String removeBlackList(String text, Set<String> blacklist) {
        text = text.replaceAll("( )+", " ");
        String [] sentences = text.split("\\.");
        StringBuilder builder = new StringBuilder();
        for ( String sentence : sentences ) {
            boolean blacklistFound = false;
            for (String bl : blacklist) {
                if (sentence.contains(bl)) {
                    blacklistFound = true;
                    break;
                }
            }
            if (!blacklistFound) {
                builder.append(sentence).append(".");
            }
        }
        return builder.toString();
    }

    private String modifyTheAcronyms(String text, Map<String, String> acronyms) {
        for (String acronym : acronyms.keySet()) {
            text = text.replaceAll(acronym, acronyms.get(acronym));
        }
        return text;
    }

    private String removeStopWords(String text, Set<String> stopwords) {
        String [] parts = text.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for ( String s : parts ) {
            if ( !stopwords.contains( s ) )
                builder.append(s).append(" ");
        }

        return builder.toString();
    }

    private String sliceCharThreshold(String text, int lowerbound, int upperbound) {

        String [] parts = text.split("\\s+");

        StringBuilder builder = new StringBuilder();
        for (String s : parts) {
            if (s.length() > lowerbound && s.length() < upperbound)
                builder.append(s).append(" ");
        }

        return builder.toString();

    }

//    private List<String> tokenize( String text, String ... delim ) {
//        String delimiter = " ";
//        if ( delim.length != 0 )
//            delimiter = delim[0];
//
//        List<String> list = new ArrayList<>();
//        StringTokenizer strTokens = new StringTokenizer(text, delimiter);
//        while ( strTokens.hasMoreTokens() ) {
//            list.add(strTokens.nextToken().trim());
//        }
//        return list;
//    }

}
