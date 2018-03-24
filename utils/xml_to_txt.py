import xml.etree.ElementTree as ET
from sklearn.feature_extraction.text import CountVectorizer
import os
import csv
import re

vectorizer = CountVectorizer(min_df=1, stop_words='english', ngram_range=(1, 1), analyzer=u'word')
analyze = vectorizer.build_analyzer()


def convert_xml_txt_questions(list_dir):
    all_domains = os.listdir(list_dir)
    for domain in all_domains:
        post_file_path = list_dir + '\\' + domain + '\\' + 'Posts.xml'
        text_file = domain + '_questions_.txt'
        text_file_path = '../text_files/' + text_file
        with open(text_file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            tree = ET.parse(post_file_path)
            root = tree.getroot()
            for child in root:
                if child.attrib['PostTypeId'] == '1':
                    text = child.attrib['Body']
                    text = re.sub(r'http\S+', '', text)
                    text = re.sub(r'@\S+', '', text)
                    text = analyze(text)
                    writer.writerow(text)


def convert_xml_txt_quest_ans(list_dir):
    all_domains = os.listdir(list_dir)
    for domain in all_domains:
        post_file_path = list_dir + '\\' + domain + '\\' + 'Posts.xml'
        tree = ET.parse(post_file_path)
        root = tree.getroot()
        post = {}
        for child in root:
            attrib = child.attrib
            text = child.attrib['Body']
            if child.attrib['PostTypeId'] == '1':
                b = post.keys()
                ggg = list(post.keys())
                if child.attrib['Id'] not in list(post.keys()):
                    post.update({child.attrib['Id']: text})
                else:
                    text = text + ' ' + post[child.attrib['Id']]
                    post.update({child.attrib['Id']: text})

            elif child.attrib['PostTypeId'] == '2':
                ggg = list(post.keys())
                if child.attrib['ParentId'] in list(post.keys()):
                    text = post[child.attrib['ParentId']] + ' ' + text
                    post.update({child.attrib['ParentId']: text})
                else:
                    post.update({child.attrib['ParentId']: text})

        text_file = domain + '_ques&ans_.txt'
        text_file_path = '../text_files/' + text_file
        with open(text_file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            for _, text in post.items():
                text = re.sub(r'http\S+', '', text)
                text = re.sub(r'@\S+', '', text)
                text = analyze(text)
                writer.writerow(text)

convert_xml_txt_quest_ans(list_dir='../RawData')
convert_xml_txt_questions(list_dir='../RawData')
