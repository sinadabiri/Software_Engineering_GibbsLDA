import xml.etree.ElementTree as ET
from sklearn.feature_extraction.text import CountVectorizer
import os
import csv


def convert_xml_txt(list_dir):
    vectorizer = CountVectorizer(min_df=1, stop_words='english', ngram_range=(1, 1), analyzer=u'word')
    analyze = vectorizer.build_analyzer()
    all_domains = os.listdir(list_dir)
    for domain in all_domains:
        post_file_path = list_dir + '\\' + domain + '\\' + 'Posts.xml'
        text_file = domain + '_' + 'Posts.txt'
        text_file_path = '../text_files/' + text_file
        with open(text_file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            tree = ET.parse(post_file_path)
            root = tree.getroot()
            for child in root:
                body_text = child.attrib['Body']
                text = analyze(body_text)
                writer.writerow(text)

convert_xml_txt(list_dir='../RawData')
