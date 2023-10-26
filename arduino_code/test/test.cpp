#include <iostream>


std::string outString;

void loop(char c) {
    if(c != ';') {
        outString += c;
        return;
    }

    std::cout << "The message was: " << outString << std::endl;
}


int main() {
    char chars[] = {'s' ,'t' ,'r' ,'i' ,'n' ,'g', ';'};

    for(char c: chars ) {
        loop(c);
    }
}