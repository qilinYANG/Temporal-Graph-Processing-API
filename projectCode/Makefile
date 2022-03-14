build:
	docker-compose build
	docker-compose up

rebuild:
	docker-compose up --build

ingress:
	javac ConnectedComponentFilesApp.java
	java ConnectedComponentFilesApp
	make clean

clean:
	rm -rf *.class