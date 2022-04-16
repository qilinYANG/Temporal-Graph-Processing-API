build:
	docker-compose build
	docker-compose up -d

rebuild:
	docker-compose up --build

ingress:
	javac GraphAnalyticsFilesApp.java
	java GraphAnalyticsFilesApp
	make clean

clean:
	rm -rf *.class