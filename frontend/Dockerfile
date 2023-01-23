FROM node:16.17.1

RUN apt upgrade -y && apt update -y

EXPOSE 4173

ENV NPM_CONFIG_LOGLEVEL info

WORKDIR /app

COPY ["package.json", "./"]

RUN npm install

COPY ["index.html", "./"]

COPY ["public/", "./public/"]

COPY ["src/", "./src/"]

RUN npm run build

USER node

CMD npm run preview
