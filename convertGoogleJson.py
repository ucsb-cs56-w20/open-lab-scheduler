#!/usr/bin/env python

# This file takes the values in heroku.json, and sets those values
# on a remote app.

# You should invoke this with
# ./setHerokuEnv.py --app APPNAME
#
# Any additional arguments passed after ./setHerokuEnv.py are passed to the "heroku config:set" commandn

import json
import os
from pprint import pprint
import sys


if len(sys.argv)!=3:
  print("Converts credentials.json from Google OAuth to format needed")
  print("  for Spring Boot SPRING_APPLICATION_JSON format")
  print("Usage: "+sys.argv[0]+" input output")
  print("   where ")
  print("      input is filename of a credentials.json file downloaded from Google OAuth configuration")
  print("      output is either localhost.json or heroku.json")
  sys.exit(1)

input_filename = sys.argv[1]
output_filename = sys.argv[2]


try:
  with open(input_filename) as input_file:
     input_dict = json.load(input_file)
except IOError as e:
  print("Error reading from", input_filename)
  print(type(e),e.args)
  sys.exit(2)
except Exception as e:
  print("Error parsing JSON from ",input_filename)
  print(type(e),e.args)
  sys.exit(3)

output_dict = {}

client_id_key = "spring.security.oauth2.client.registration.google.client-id"
client_secret_key = "spring.security.oauth2.client.registration.google.client-secret"


output_dict[client_id_key] = input_dict["web"]["client_id"]
output_dict[client_secret_key] = input_dict["web"]["client_secret"]


try:
  with open(output_filename,'w') as output_file:
    json.dump(output_dict, output_file)
except Exception as e:
  print("Error writing to ", output_filename)
  print(type(e),e.args)
  sys.exit(4)

