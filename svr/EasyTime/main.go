package main

import (
	_ "EasyTime/internal/packed"

	"github.com/gogf/gf/v2/os/gctx"

	"EasyTime/internal/cmd"
)

func main() {
	cmd.Main.Run(gctx.New())
}
