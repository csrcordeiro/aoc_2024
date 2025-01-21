package common

import (
	"os"
)

func ReadInput(fileName string) string {
	content, err := os.ReadFile("../resources/" + fileName)
	if err != nil {
		panic("Could not read file " + fileName)
	}

	return string(content)
}
