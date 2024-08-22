import { MouseEvent } from "react";

export function preventDefaultClick(e: MouseEvent<Element>) {
    e.preventDefault();
    e.stopPropagation();
}