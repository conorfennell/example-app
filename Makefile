upload-api:
	apibuilder upload paintbatch-com example-app api/example-app.json --update-config --version "$(ver)"

update-generated-src:
	apibuilder update