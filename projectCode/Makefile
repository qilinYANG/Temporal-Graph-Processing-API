build:
	docker-compose build
	docker-compose up

rebuild:
	docker-compose up --build

ingress:
	javac GraphAnalyticsFilesApp.java
	java GraphAnalyticsFilesApp
	make clean

clean:
	rm -rf *.class