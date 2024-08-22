export function getFilteredItems<T>(items: T[], filter: string, key: keyof T) {
    if (!filter) return items;
    return items.filter(item => (item[key] as string).toLowerCase().includes(filter));
}