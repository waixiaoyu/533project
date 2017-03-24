package edu.stanford.nlp;

import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

class Tagging {

	private static final String PARSE_MODEL_PATH = "englishPCFG.ser.gz";

	private static LexicalizedParser lp;

	static {

		lp = LexicalizedParser.loadModel(PARSE_MODEL_PATH);
	}

	public static void main(String[] args) {
		Tagging t = new Tagging();
		String sent = "Showers continued throughout the week in the Bahia cocoa zone.";
		List l = t.searchByTag(t.TaggingSentence(sent), "NN");
		System.out.println(l);
	}

	/**
	 * demoAPI demonstrates other ways of calling the parser with already
	 * tokenized text, or in some cases, raw text that needs to be tokenized as
	 * a single sentence. Output is handled with a TreePrint object. Note that
	 * the options used when creating the TreePrint can determine what results
	 * to print out. Once again, one can capture the output by passing a
	 * PrintWriter to TreePrint.printTree. This code is for English.
	 */
	private List<TaggedWord> TaggingSentence(String sent) {

		// This option shows loading and using an explicit tokenizer
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(sent));
		List<CoreLabel> rawWords = tok.tokenize();
		Tree parse = lp.apply(rawWords);
		List<TaggedWord> taggedWords = parse.taggedYield();
		System.out.println(taggedWords);
		return taggedWords;
	}

	private List<String> searchByTag(List<TaggedWord> taggedWords, String pre) {
		List<String> list = new LinkedList<>();
		for (TaggedWord tw : taggedWords) {
			if (tw.tag().startsWith(pre)) {
				list.add(tw.word());
			}
		}
		return list;
	}

	/**
	 * CC Coordinating conjunction; CD Cardinal number; DT Determiner; EX
	 * Existential there; FW Foreign word; IN Preposition or subordinating
	 * conjunction; JJ Adjective; JJR Adjective, comparative; JJS Adjective,
	 * superlative; LS List item marker; MD Modal; NN Noun, singular or mass;
	 * NNS Noun, plural; NNP Proper noun, singular; NNPS Proper noun, plural;
	 * PDT Predeterminer; POS Possessive ending; PRP Personal pronoun; PRP$
	 * Possessive pronoun; RB Adverb; RBR Adverb, comparative; RBS Adverb,
	 * superlative; RP Particle; SYM Symbol; TO to; UH Interjection; VB Verb,
	 * base form; VBD Verb, past tense; VBG Verb, gerund or present participle;
	 * VBN Verb, past participle; VBP Verb, non­3rd person singular present; VBZ
	 * Verb, 3rd person singular present; WDT Wh­determiner; WP Wh­pronoun; WP$
	 * Possessive wh­pronoun; WRB Wh­adverb;
	 */
	private Tagging() {
	} // static methods only

}
